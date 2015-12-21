package gigigo.com.orchextra.data.datasources.api.model.resquests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import gigigo.com.orchextra.data.datasources.api.model.responses.ApiPoint;

/**
 * Created by Sergio Martinez Rodriguez
 * Date 15/12/15.
 */
public class ApiGeoLocation {

  @Expose @SerializedName("country")
  private String country;

  @Expose @SerializedName("countryCode")
  private String countryCode;

  @Expose @SerializedName("locality")
  private String locality;

  @Expose @SerializedName("zip")
  private int zip;

  @Expose @SerializedName("street")
  private String street;

  @Expose @SerializedName("point")
  private ApiPoint point;

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public int getZip() {
    return zip;
  }

  public void setZip(int zip) {
    this.zip = zip;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public ApiPoint getPoint() {
    return point;
  }

  public void setPoint(ApiPoint point) {
    this.point = point;
  }
}
