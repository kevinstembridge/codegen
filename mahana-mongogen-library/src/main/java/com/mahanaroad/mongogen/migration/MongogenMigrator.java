package com.mahanaroad.mongogen.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class MongogenMigrator implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongogenMigrator.class);

    private final MongogenMigrationLockDao mongogenMigrationLockDao;
    private final MongogenMigrationEntryDao mongogenMigrationEntryDao;

    private ApplicationContext applicationContext;


    @Autowired
    public MongogenMigrator(MongogenMigrationLockDao mongogenMigrationLockDao, MongogenMigrationEntryDao mongogenMigrationEntryDao) {

        this.mongogenMigrationLockDao = mongogenMigrationLockDao;
        this.mongogenMigrationEntryDao = mongogenMigrationEntryDao;

    }


    @Override
    public void afterPropertiesSet() {

        runMigrations();

    }


    public void runMigrations() {

        final Collection<MongogenMigration> migrations = findAllMigrations();

        try {
            obtainLock();
        } catch (MongogenMigrationLockNotAvailableException e) {
            LOGGER.info(e.getMessage());
            return;
        }

        try {

            migrations
                    .stream()
                    .sorted(new MongogenMigrationComparator())
                    .forEach(this::runMigration);

        } finally {
            releaseLock();
        }

    }


    private void runMigration(final MongogenMigration migration) {

        if (migrationShouldBeRun(migration)) {
            migration.applyMigration();
            recordMigrationEntry(migration);
        }

    }


    private boolean migrationShouldBeRun(final MongogenMigration migration) {

        final MongogenMigrationId migrationId = migration.getId();
        return this.mongogenMigrationEntryDao.notExistsById(migrationId);

    }


    private Collection<MongogenMigration> findAllMigrations() {

        final boolean includeNonSingletons = false;
        final boolean allowEagerInit = true;
        final Map<String, MongogenMigration> migrations = this.applicationContext.getBeansOfType(MongogenMigration.class, includeNonSingletons, allowEagerInit);
        return migrations.values();

    }


    private void obtainLock() throws MongogenMigrationLockNotAvailableException {

        this.mongogenMigrationLockDao.obtainLock();
        LOGGER.info("Mongogen migration lock obtained.");

    }


    private void releaseLock() {

        this.mongogenMigrationLockDao.releaseLock();
        LOGGER.info("Mongogen migration lock released.");

    }


    private void recordMigrationEntry(final MongogenMigration migration) {

        this.mongogenMigrationEntryDao.insert(migration);

    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;

    }


}
