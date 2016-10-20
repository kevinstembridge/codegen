package com.mahanaroad.mongogen.migration;

import java.util.Comparator;

public class MongogenMigrationComparator implements Comparator<MongogenMigration> {


    @Override
    public int compare(MongogenMigration o1, MongogenMigration o2) {

        return Comparator.comparing(MongogenMigration::getId).compare(o1, o2);

    }


}
