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

package com.gigigo.orchextra;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class ORCUser {

    private final String crmId;
    private final GregorianCalendar birthdate;
    private final Gender gender;
    private List<String> keywords;
    private final List<ORCUserTag> tags;

  /**
   * Creates an orchextra user, this user will be useful for segmentation purposes and statistic
   * tracking in dashboard
   * @param crmId Crm ID, can be the user name of your app
   * @param birthdate user's birth date.
   * @param gender user's male, using an enum
   * @param keywords important words in order to segment specific actions
   */
    //TODO enable deprecation strategy when tags will be ready
    public ORCUser(String crmId, GregorianCalendar birthdate, Gender gender, List<String> keywords) {
        this(crmId, birthdate, gender);
        this.keywords = keywords;
    }

    /**
     * Creates an orchextra user, this user will be useful for segmentation purposes and statistic
     * tracking in dashboard
     * @param crmId Crm ID, can be the user name of your app
     * @param birthdate user's birth date.
     * @param gender user's male, using an enum
     * @param tags important Tags in order to segment specific actions
     */
    public ORCUser(String crmId, GregorianCalendar birthdate, Gender gender, ORCUserTag...tags) {
        this.crmId = crmId;
        this.birthdate = birthdate;
        this.gender = gender;
        this.tags = Arrays.asList(tags);
        this.keywords = Collections.EMPTY_LIST;
    }

    public String getCrmId() {
        return crmId;
    }

    public GregorianCalendar getBirthdate() {
        return birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<ORCUserTag> getTags() {
        return tags;
    }

    public enum Gender {
        ORCGenderMale,
        ORCGenderFemale
    }
}
