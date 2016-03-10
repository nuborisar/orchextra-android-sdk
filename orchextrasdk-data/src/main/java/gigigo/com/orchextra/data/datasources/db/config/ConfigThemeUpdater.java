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

package gigigo.com.orchextra.data.datasources.db.config;

import com.gigigo.ggglib.mappers.Mapper;
import com.gigigo.orchextra.domain.model.vo.Theme;
import gigigo.com.orchextra.data.datasources.db.model.ThemeRealm;
import io.realm.Realm;
import io.realm.RealmResults;

public class ConfigThemeUpdater {

  private final Mapper<Theme, ThemeRealm> themeRealmMapper;

  public ConfigThemeUpdater(Mapper<Theme, ThemeRealm> themeRealmMapper) {
    this.themeRealmMapper = themeRealmMapper;
  }

  public Theme saveTheme(Realm realm, Theme theme) {
    boolean hasChangedTheme = false;

    if (theme != null) {
      hasChangedTheme = checkIfChangedTheme(realm, theme);
    } else {
      realm.clear(ThemeRealm.class);
    }
    if (hasChangedTheme) {
      return theme;
    } else {
      return null;
    }
  }

  private boolean checkIfChangedTheme(Realm realm, Theme theme) {
    boolean hasChangedTheme = false;

    ThemeRealm themeRealm = themeRealmMapper.modelToExternalClass(theme);

    RealmResults<ThemeRealm> savedTheme = realm.where(ThemeRealm.class).findAll();

    if (savedTheme.size() > 0) {
      hasChangedTheme = !checkThemeAreEquals(themeRealm, savedTheme.first());
    }

    if (hasChangedTheme) {
      realm.clear(ThemeRealm.class);
      realm.copyToRealm(themeRealm);
    }
    return hasChangedTheme;
  }

  private boolean checkThemeAreEquals(ThemeRealm themeRealm, ThemeRealm oldTheme) {
    if (oldTheme != null) {
      return themeRealm.getPrimaryColor().equals(oldTheme.getPrimaryColor())
          && themeRealm.getSecondaryColor().equals(oldTheme.getSecondaryColor());
    } else {
      return false;
    }
  }
}
