package com.mahanaroad.mongogen.sample.party;

import com.mahanaroad.mongogen.AbstractIntegrationTest;
import com.mahanaroad.mongogen.sample.org.OrganizationDao;
import com.mahanaroad.mongogen.sample.org.OrganizationEntity;
import com.mahanaroad.mongogen.sample.person.PersonDao;
import com.mahanaroad.mongogen.sample.person.PersonEntity;
import com.mahanaroad.mongogen.sample.user.UserDao;
import com.mahanaroad.mongogen.sample.user.UserEntity;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static com.mahanaroad.mongogen.sample.org.OrganizationEntityTestBuilder.anOrganization;
import static com.mahanaroad.mongogen.sample.person.PersonEntityTestBuilder.aPerson;
import static com.mahanaroad.mongogen.sample.user.UserEntityTestBuilder.aUser;
import static org.testng.Assert.assertEquals;


public class PartyDaoTest extends AbstractIntegrationTest {

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private MongoClient mongoClient;


    @Test
    public void testInsertAndFindById() {

        final OrganizationEntity organizationEntity = anOrganization().build();
        this.organizationDao.insert(organizationEntity);

        final PersonEntity personEntity = aPerson().build();
        this.personDao.insert(personEntity);

        final UserEntity userEntity = aUser().build();
        this.userDao.insert(userEntity);

        final OrganizationEntity actualOrganization = (OrganizationEntity) this.partyDao.findById(organizationEntity.getId());

        assertEquals(actualOrganization.getCreatedTimestampUtc(), organizationEntity.getCreatedTimestampUtc());
        assertEquals(actualOrganization.getName(), organizationEntity.getName());

        final PersonEntity actualPerson = (PersonEntity) this.partyDao.findById(personEntity.getId());

        assertEquals(actualPerson.getCreatedTimestampUtc(), personEntity.getCreatedTimestampUtc());
        assertEquals(actualPerson.getFirstName(), personEntity.getFirstName());
        assertEquals(actualPerson.getLastName(), personEntity.getLastName());

        final UserEntity actualUser = (UserEntity) this.partyDao.findById(userEntity.getId());

        assertEquals(actualUser.getCreatedTimestampUtc(), userEntity.getCreatedTimestampUtc());
        assertEquals(actualUser.getFirstName(), userEntity.getFirstName());
        assertEquals(actualUser.getLastName(), userEntity.getLastName());
        assertEquals(actualUser.getEncryptedPassword(), userEntity.getEncryptedPassword());

    }


}
