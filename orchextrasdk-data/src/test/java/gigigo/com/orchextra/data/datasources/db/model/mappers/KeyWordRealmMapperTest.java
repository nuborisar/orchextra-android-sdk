package gigigo.com.orchextra.data.datasources.db.model.mappers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import gigigo.com.orchextra.data.datasources.db.model.KeyWordRealm;
import io.realm.RealmList;

import static org.junit.Assert.assertEquals;

public class KeyWordRealmMapperTest {

    @Test
    public void shouldMapModelToData() throws Exception {
        String keyword = "keyword";

        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        KeyWordRealm keyWordRealm = mapper.modelToExternalClass(keyword);

        assertEquals("keyword", keyWordRealm.getKeyword());
    }

    @Test
    public void souldMapDataToModel() throws Exception {
        KeyWordRealm keyWordRealm = new KeyWordRealm();
        keyWordRealm.setKeyword("keyword");

        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        String keyword = mapper.externalClassToModel(keyWordRealm);

        assertEquals("keyword", keyword);
    }

    @Test
    public void shouldMapListKeywordsFromModelToData() throws Exception {
        List<String> keywords = new ArrayList<>();
        keywords.add("keyword1");
        keywords.add("keyword2");

        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        RealmList<KeyWordRealm> keyWordRealms = mapper.stringKeyWordsToRealmList(keywords);

        assertEquals(2, keyWordRealms.size());
        assertEquals("keyword1", keyWordRealms.get(0).getKeyword());
        assertEquals("keyword2", keyWordRealms.get(1).getKeyword());
    }

    @Test
    public void shouldMapEmptyKeywordsListWhenIsNull() throws Exception {
        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        RealmList<KeyWordRealm> keyWordRealms = mapper.stringKeyWordsToRealmList(null);

        assertEquals(0, keyWordRealms.size());
    }

    @Test
    public void shouldMapListRealmKeywordsFromDataToModel() throws Exception {
        RealmList<KeyWordRealm> keyWordRealm = new RealmList<>();
        keyWordRealm.add(new KeyWordRealm("keyword1"));
        keyWordRealm.add(new KeyWordRealm("keyword2"));

        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        List<String> stringList = mapper.realmKeyWordsToStringList(keyWordRealm);

        assertEquals(2, stringList.size());
        assertEquals("keyword1", stringList.get(0));
        assertEquals("keyword2", stringList.get(1));
    }

    @Test
    public void shouldReturnRealmKeyWordsWhenKeyWordRealmsIsNull() throws Exception {
        KeyWordRealmMapper mapper = new KeyWordRealmMapper();
        List<String> keywords = mapper.realmKeyWordsToStringList(null);

        assertEquals(0, keywords.size());
    }
}