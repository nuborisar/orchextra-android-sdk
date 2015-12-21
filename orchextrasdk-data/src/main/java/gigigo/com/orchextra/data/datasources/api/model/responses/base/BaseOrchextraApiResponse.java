package gigigo.com.orchextra.data.datasources.api.model.responses.base;

import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.HttpResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 30/11/15.
 */
public class BaseOrchextraApiResponse<JSONData> implements
    ApiGenericResponse<JSONData, OrchextraApiErrorResponse> {

  @SerializedName("status")
  private boolean status;
  @SerializedName("data")
  private JSONData data;
  @SerializedName("error")
  private OrchextraApiErrorResponse error;

  private HttpResponse httpResponse;

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  @Override public HttpResponse getHttpResponse() {
    return httpResponse;
  }

  @Override public void setHttpResponse(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  @Override public boolean isException() {
    return false;
  }

  @Override public JSONData getResult() {
    return data;
  }

  @Override public void setResult(JSONData data) {
    this.data = data;
  }

  @Override public void setBusinessError(OrchextraApiErrorResponse error) {
    this.error = error;
  }

  @Override public OrchextraApiErrorResponse getBusinessError() {
    return error;
  }
}
