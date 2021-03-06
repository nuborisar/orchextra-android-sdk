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

package com.gigigo.orchextra.domain.model.entities.authentication;

import com.gigigo.orchextra.domain.model.GenderType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Crm {

  private String crmId;
  private GenderType gender;
  private Date birthDate;
  private List<String> keywords;
  private List<CrmTag> tags;

  public Crm() {
  }

  public Crm(String crmId, GenderType gender, Date birthDate, List<String> keywords, List<CrmTag> tags) {
    this.crmId = crmId;
    this.gender = gender;
    this.birthDate = birthDate;
    this.keywords = keywords;
    this.tags = tags;
  }

  public Crm(String crmId, GenderType gender, Date birthDate, List<String> keywords) {
    this(crmId, gender, birthDate, keywords, Collections.EMPTY_LIST);
  }

  public String getCrmId() {
    return crmId;
  }

  public void setCrmId(String crmId) {
    this.crmId = crmId;
  }

  public GenderType getGender() {
    return gender;
  }

  public void setGender(GenderType gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public List<CrmTag> getTags() {
    return tags;
  }

  public void setTags(List<CrmTag> tags) {
    this.tags = tags;
  }

  public boolean isEquals(String crmId) {
    if (this.crmId == null) {
      return crmId == null;
    } else {
      return this.crmId.equals(crmId);
    }
  }

}
