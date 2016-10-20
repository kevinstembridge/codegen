package com.mahanaroad.mongogen.migration;

import java.util.Optional;

public interface MongogenMigration {


    public MongogenMigrationId getId();


    public Optional<String> getChangeDescription();


    public void applyMigration();


}
