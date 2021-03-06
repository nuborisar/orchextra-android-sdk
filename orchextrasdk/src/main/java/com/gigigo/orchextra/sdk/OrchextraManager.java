/*
 * Created by Orchextra
 *
 * Copyright (C) 2016 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gigigo.orchextra.sdk;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.imagerecognitioninterface.ImageRecognition;
import com.gigigo.orchextra.BuildConfig;
import com.gigigo.orchextra.ORCUser;
import com.gigigo.orchextra.OrchextraLogLevel;
import com.gigigo.orchextra.R;
import com.gigigo.orchextra.control.controllers.authentication.SaveUserController;
import com.gigigo.orchextra.control.controllers.status.SdkAlreadyStartedException;
import com.gigigo.orchextra.control.controllers.status.SdkInitializationException;
import com.gigigo.orchextra.control.controllers.status.SdkNotInitializedException;
import com.gigigo.orchextra.device.imagerecognition.ImageRecognitionManager;
import com.gigigo.orchextra.di.components.DaggerOrchextraComponent;
import com.gigigo.orchextra.di.components.OrchextraComponent;
import com.gigigo.orchextra.di.injector.InjectorImpl;
import com.gigigo.orchextra.di.modules.OrchextraModule;
import com.gigigo.orchextra.domain.abstractions.actions.CustomOrchextraSchemeReceiver;
import com.gigigo.orchextra.domain.abstractions.device.OrchextraSDKLogLevel;
import com.gigigo.orchextra.domain.abstractions.device.OrchextraLogger;
import com.gigigo.orchextra.domain.abstractions.initialization.OrchextraManagerCompletionCallback;
import com.gigigo.orchextra.domain.abstractions.initialization.OrchextraStatusAccessor;
import com.gigigo.orchextra.domain.abstractions.initialization.StartStatusType;
import com.gigigo.orchextra.domain.abstractions.lifecycle.AppRunningMode;
import com.gigigo.orchextra.domain.abstractions.lifecycle.AppStatusEventsListener;
import com.gigigo.orchextra.domain.model.entities.authentication.Crm;
import com.gigigo.orchextra.domain.model.triggers.params.AppRunningModeType;
import com.gigigo.orchextra.sdk.application.applifecycle.OrchextraActivityLifecycle;
import com.gigigo.orchextra.sdk.model.OrcUserToCrmConverter;
import com.gigigo.orchextra.sdk.scanner.ScannerManager;

import orchextra.javax.inject.Inject;

public class OrchextraManager {

    private static OrchextraSDKLogLevel orchextraSDKLogLevel =
            (BuildConfig.DEBUG) ? OrchextraSDKLogLevel.ALL : OrchextraSDKLogLevel.NONE;

    private static OrchextraManager instance;
    private InjectorImpl injector;
    private OrchextraManagerCompletionCallback orchextraCompletionCallback;

    @Inject
    OrchextraActivityLifecycle orchextraActivityLifecycle;
    @Inject
    OrchextraTasksManager orchextraTasksManager;
    @Inject
    OrcUserToCrmConverter orcUserToCrmConverter;
    @Inject
    OrchextraStatusAccessor orchextraStatusAccessor;
    @Inject
    SaveUserController saveUserController;
    @Inject
    AppRunningMode appRunningMode;
    @Inject
    AppStatusEventsListener appStatusEventsListener;
    @Inject
    ScannerManager scannerManager;
    @Inject
    ImageRecognitionManager imageRecognitionManager;
    @Inject
    OrchextraLogger orchextraLogger;

    /**
     * Fist call to orchextra, it is compulsory call this for starting to do any sdk Stuff
     *
     * @param application
     * @param orchextraCompletionCallback
     */
    public static void sdkInit(Application application, OrchextraManagerCompletionCallback orchextraCompletionCallback) {
        OrchextraManager.instance = new OrchextraManager();
        OrchextraManager.instance.init(application, orchextraCompletionCallback);
    }

    /**
     * This method is called from client app in order to start application at one concrete moment,
     * this is not dependant on context neither app lifecycle, could be called in any moment.
     *
     * @param apiKey    credentials
     * @param apiSecret credentials
     */
    public static synchronized void sdkStart(String apiKey, String apiSecret) {
        if (OrchextraManager.instance != null &&
                AndroidSdkVersion.hasJellyBean18()) {
            OrchextraManager.instance.start(apiKey, apiSecret);
        }
    }

    /**
     * Called for inform sdk about client app user information, useful for tacking segmentation about
     * users. This call can provokes call to configuration
     *
     * @param user information about client app user
     */
    public static synchronized void setUser(ORCUser user) {
        OrchextraManager orchextraManager = OrchextraManager.instance;
        if (orchextraManager != null &&
                AndroidSdkVersion.hasJellyBean18()) {
            if (orchextraManager.orchextraStatusAccessor.isStarted()) {
                OrcUserToCrmConverter orcUserToCrmConverter = orchextraManager.orcUserToCrmConverter;
                SaveUserController saveUserController = orchextraManager.saveUserController;

                Crm crm = orcUserToCrmConverter.convertOrcUserToCrm(user);
                saveUserController.saveUser(crm);
            } else {
                //TODO could be nice the idea of just store user in local storage if sdk is not running
                orchextraManager.orchextraCompletionCallback.onInit("Not started SDK, "
                        + "must have SDK started before calling set user");
            }
        }
    }

    /**
     * Called for set custom scheme receiver
     *
     * @param customSchemeReceiver custom scheme receiver
     */
    public static synchronized void setCustomSchemeReceiver(CustomOrchextraSchemeReceiver customSchemeReceiver) {
        OrchextraModule orchextraModule = getOrchextraModule();
        if (orchextraModule != null) {
            orchextraModule.setCustomSchemeReceiver(customSchemeReceiver);
        }
    }

    /**
     * Called when client app want to stop all orchextra proccess
     */
    public static synchronized void sdkStop() {
        OrchextraManager orchextraManager = OrchextraManager.instance;
        if (orchextraManager != null && orchextraManager.orchextraStatusAccessor.isStarted()) {
            orchextraManager.orchextraStatusAccessor.setStoppedStatus();
            orchextraManager.instance.stopOrchextraTasks();
        }
    }

    /**
     * Internal sdk dependency injector
     *
     * @return dependency injector
     */
    public static InjectorImpl getInjector() {
        if (OrchextraManager.instance != null) {
            return OrchextraManager.instance.injector;
        }
        return null;
    }

    public static void setLogLevel(OrchextraLogLevel logLevel) {
        orchextraSDKLogLevel = logLevel.getSDKLogLevel();
    }

    public static OrchextraSDKLogLevel getLogLevel() {
        return orchextraSDKLogLevel;
    }

    private void stopOrchextraTasks() {
        orchextraTasksManager.stopAllTasks();

        if (appRunningMode.getRunningModeType() == AppRunningModeType.BACKGROUND) {
            appStatusEventsListener.onBackgroundEnd();
        }
    }

    /**
     * Fist call to orchextra, it is compulsory call this for starting to do any sdk Stuff
     *
     * @param app
     * @param completionCallback
     */
    private void init(Application app, OrchextraManagerCompletionCallback completionCallback) {

        orchextraCompletionCallback = completionCallback;

        if (AndroidSdkVersion.hasJellyBean18()) {
            initDependencyInjection(app.getApplicationContext(), completionCallback);
            initLifecyle(app);
            //initialize();
            orchextraStatusAccessor.initialize();
            completionCallback.onInit("OK");
        } else {
            completionCallback.onError(app.getString(R.string.ox_not_supported_android_sdk));
        }
    }

    private void initDependencyInjection(Context applicationContext,
                                         OrchextraManagerCompletionCallback orchextraCompletionCallback) {

        OrchextraComponent orchextraComponent = DaggerOrchextraComponent.builder()
                .orchextraModule(new OrchextraModule(applicationContext, orchextraCompletionCallback))
                .build();

        injector = new InjectorImpl(orchextraComponent);
        orchextraComponent.injectOrchextra(OrchextraManager.instance);
    }

    private void start() {
        new Thread(getStartRunnable()).start();
    }

    private Runnable getStartRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                startSDK();
            }
        };
    }

    private void startSDK() {
        if (appRunningMode.getRunningModeType() == AppRunningModeType.FOREGROUND) {
            appStatusEventsListener.onForegroundStart();
        } else if (appRunningMode.getRunningModeType() == AppRunningModeType.BACKGROUND) {
            appStatusEventsListener.onBackgroundStart();
        }
        orchextraCompletionCallback.onSuccess();
    }

    /**
     * This method is called from client app in order to start application at one concrete moment,
     * this is not dependant on context neither app lifecycle, could be called in any moment.
     *
     * @param apiKey
     * @param apiSecret
     */
    private void start(String apiKey, String apiSecret) {
        Exception exception = null;
        try {
            StartStatusType status = orchextraStatusAccessor.setStartedStatus(apiKey, apiSecret);

            if (status == StartStatusType.SDK_READY_FOR_START) {
                start();
            }

            if (status == StartStatusType.SDK_WAS_ALREADY_STARTED_WITH_DIFERENT_CREDENTIALS) {
                //TODO restart or call any service???
            }

        } catch (SdkAlreadyStartedException alreadyStartedException) {
            orchextraLogger.log(alreadyStartedException.getMessage(), OrchextraSDKLogLevel.WARN);
            orchextraCompletionCallback.onInit(alreadyStartedException.getMessage());

        } catch (SdkNotInitializedException notInitializedException) {
            exception = notInitializedException;

        } catch (SdkInitializationException initializationException) {
            exception = initializationException;
            exception.printStackTrace();

        } finally {
            if (exception != null) {
                orchextraLogger.log(exception.getMessage(), OrchextraSDKLogLevel.ERROR);
                orchextraCompletionCallback.onError(exception.getMessage());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initLifecyle(Application app) {
        app.registerActivityLifecycleCallbacks(orchextraActivityLifecycle);
    }

    public static void openScannerView() {
        OrchextraManager orchextraManager = OrchextraManager.instance;
        OrchextraModule orchextraModule = getOrchextraModule();
        if (orchextraModule != null) {
            ScannerManager scannerManager = orchextraManager.scannerManager;
            scannerManager.open();
        }
    }

    private static OrchextraModule getOrchextraModule() {
        if (getInjector() != null) {
            OrchextraComponent orchextraComponent = getInjector().getOrchextraComponent();
            return orchextraComponent.getOrchextraModule();
        } else {
            return null;
        }
    }

    public static void setImageRecognition(ImageRecognition imageRecognition) {
        if (OrchextraManager.instance != null) {
            OrchextraManager.instance.imageRecognitionManager.setImplementation(imageRecognition);
        }

    }

    public static void startImageRecognition() {
        if (OrchextraManager.instance != null) {
            OrchextraManager.instance.imageRecognitionManager.startImageRecognition();
        }
    }
}
