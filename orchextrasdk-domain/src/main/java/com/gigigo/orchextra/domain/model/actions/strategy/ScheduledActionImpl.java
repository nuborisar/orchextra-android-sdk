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

package com.gigigo.orchextra.domain.model.actions.strategy;

import com.gigigo.orchextra.domain.model.actions.ScheduledAction;

public class ScheduledActionImpl implements ScheduledAction {

  private final BasicAction basicAction;
  private final ScheduleFunctionality scheduleFunctionality;

  public ScheduledActionImpl(BasicAction basicAction) {
    this.scheduleFunctionality = basicAction.scheduleFunctionality;
    this.basicAction = basicAction;
  }

  @Override public boolean isCancelable() {
    return scheduleFunctionality.isCancelable();
  }

  @Override public long getScheduleTime() {
    return scheduleFunctionality.getScheduleTime();
  }

  @Override public String getId() {
    return basicAction.getId();
  }

  @Override public String getEventId() {
    return scheduleFunctionality.getSchedule().getEventId();
  }

  @Override public void setEventId(String id) {
    scheduleFunctionality.getSchedule().setEventId(id);
  }

  @Override public BasicAction getBasicAction() {
    return basicAction;
  }
}
