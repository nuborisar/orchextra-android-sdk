package com.gigigo.orchextra.domain.services.proximity;

import com.gigigo.gggjavalib.business.model.BusinessError;
import com.gigigo.gggjavalib.business.model.BusinessObject;
import com.gigigo.orchextra.domain.dataprovider.ProximityLocalDataProvider;
import com.gigigo.orchextra.domain.interactors.base.InteractorResponse;
import com.gigigo.orchextra.domain.interactors.geofences.errors.DeleteGeofenceEventError;
import com.gigigo.orchextra.domain.interactors.geofences.errors.RetrieveGeofenceItemError;
import com.gigigo.orchextra.domain.model.entities.proximity.OrchextraGeofence;
import com.gigigo.orchextra.domain.model.triggers.params.GeoPointEventType;
import com.gigigo.orchextra.domain.services.DomaninService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 15/2/16.
 */
public class GeofenceCheckerService implements DomaninService {

  private final ProximityLocalDataProvider proximityLocalDataProvider;

  public GeofenceCheckerService(ProximityLocalDataProvider proximityLocalDataProvider) {
    this.proximityLocalDataProvider = proximityLocalDataProvider;
  }

  public InteractorResponse<List<OrchextraGeofence>> obtainCheckedGeofences(List<String> triggeringGeofenceIds,
                                                                            GeoPointEventType geofenceTransition) {

  if (geofenceTransition == GeoPointEventType.ENTER) {
      return new InteractorResponse(storeGeofences(triggeringGeofenceIds));
    } else {
      return new InteractorResponse(deleteStoredGeofences(triggeringGeofenceIds));
    }
  }

  private List<OrchextraGeofence> storeGeofences(List<String> triggeringGeofenceIds) {

    List<OrchextraGeofence> addedGeofenceList = new ArrayList<>();

    for (String triggeringGeofenceId : triggeringGeofenceIds) {
      InteractorResponse<OrchextraGeofence> interactorResponse = storeGeofence(triggeringGeofenceId);
      if (!interactorResponse.hasError()) {
        addedGeofenceList.add(interactorResponse.getResult());
      }
    }

    return addedGeofenceList;
  }

  private InteractorResponse<OrchextraGeofence> storeGeofence(String triggeringGeofenceId) {
    BusinessObject<OrchextraGeofence> bo = proximityLocalDataProvider.obtainGeofenceEvent(triggeringGeofenceId);

    if (!bo.isSuccess()) {
      return new InteractorResponse(new RetrieveGeofenceItemError(BusinessError.createKoInstance(bo.getBusinessError().getMessage())));
    }else{
      bo = proximityLocalDataProvider.storeGeofenceEvent(triggeringGeofenceId);
      return new InteractorResponse(bo.getData());
    }
  }

  private List<OrchextraGeofence> deleteStoredGeofences(List<String> triggeringGeofenceIds) {

    List<OrchextraGeofence> removedGeofenceList = new ArrayList<>();

    for (String triggeringGeofenceId : triggeringGeofenceIds) {
      InteractorResponse<OrchextraGeofence> interactorResponse = deleteStoredGeofence(triggeringGeofenceId);
      if (interactorResponse.hasError()) {
        removedGeofenceList.add(interactorResponse.getResult());
      }
    }

    return removedGeofenceList;
  }

  private InteractorResponse<OrchextraGeofence> deleteStoredGeofence(String triggeringGeofenceId) {
    BusinessObject<OrchextraGeofence> bo = proximityLocalDataProvider.deleteGeofenceEvent(triggeringGeofenceId);
    if (!bo.isSuccess()){
      return new InteractorResponse(new DeleteGeofenceEventError(BusinessError.createKoInstance(bo.getBusinessError().getMessage())));
    }else{
      return new InteractorResponse(bo.getData());
    }
  }
}
