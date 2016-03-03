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

package com.gigigo.orchextra.device;

import android.os.Bundle;

import com.gigigo.ggglib.ContextProvider;
import com.gigigo.ggglib.permissions.PermissionChecker;
import com.gigigo.ggglib.permissions.UserPermissionRequestResponseListener;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.device.permissions.PermissionLocationImp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GoogleApiClientConnector implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final ContextProvider contextProvider;
    private final PermissionChecker permissionChecker;
    private final PermissionLocationImp accessFineLocationPermissionImp;

    private GoogleApiClient client;
    private OnConnectedListener onConnectedListener;

    public GoogleApiClientConnector(ContextProvider contextProvider, PermissionChecker permissionChecker,
                                    PermissionLocationImp accessFineLocationPermissionImp) {
        this.contextProvider = contextProvider;
        this.permissionChecker = permissionChecker;
        this.accessFineLocationPermissionImp = accessFineLocationPermissionImp;
    }

    public void connect() {
        if (contextProvider.getApplicationContext() != null) {
            client = new GoogleApiClient.Builder(contextProvider.getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            client.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        GGGLogImpl.log("onConnected");

        if (onConnectedListener != null) {
            onConnectedListener.onConnected(bundle);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        GGGLogImpl.log("onConnectionSuspended: Called when the client is temporarily in a disconnected state");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        GGGLogImpl.log("onConnectionFailed");

        switch (connectionResult.getErrorCode()) {
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                GGGLogImpl.log("Necesita actualizar google play");
                break;
            case ConnectionResult.SERVICE_MISSING_PERMISSION:
                GGGLogImpl.log("Faltan permisos: ACCESS_FINE_LOCATION");
                boolean isGranted = permissionChecker.isGranted(accessFineLocationPermissionImp);
                if (isGranted) {
                    connect();
                } else {
                    if (contextProvider.getCurrentActivity() != null) {
                        permissionChecker.askForPermission(accessFineLocationPermissionImp, userPermissionResponseListener, contextProvider.getCurrentActivity());
                    }
                }
                break;
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return client;
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void disconnected() {
        if (client != null) {
            client.disconnect();
        }
    }

    public interface OnConnectedListener {
        void onConnected(Bundle bundle);
    }

    public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
        this.onConnectedListener = onConnectedListener;
    }

    private UserPermissionRequestResponseListener userPermissionResponseListener = new UserPermissionRequestResponseListener() {
        @Override
        public void onPermissionAllowed(boolean permissionAllowed) {
            if (permissionAllowed) {
                connect();
            }
        }
    };
}
