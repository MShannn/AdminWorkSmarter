package com.jvidal.worksmarter.RealmDatabase;

import io.realm.RealmObject;

public class TypesOFProblemDatabase extends RealmObject {


    String structureType;
    String anomalyType;
    String problems;

    public TypesOFProblemDatabase() {
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(String anomalyType) {
        this.anomalyType = anomalyType;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }
}
