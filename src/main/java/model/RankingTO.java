/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author alu2014080
 */
public class RankingTO implements Comparable<RankingTO> {

    public String employee;
    public int numIncidences;

    public RankingTO(String employee, int numIncidences) {
        this.employee = employee;
        this.numIncidences = numIncidences;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getNumIncidences() {
        return numIncidences;
    }

    public void setNumIncidences(int numIncidences) {
        this.numIncidences = numIncidences;
    }

    @Override
    public String toString() {
        return "Employee: " + employee + "\n\t"
                + "Nº urgent incidences: " + numIncidences;
    }

    @Override
    public int compareTo(RankingTO comparestu) {
        int compare = ((RankingTO) comparestu).getNumIncidences();
        return compare - this.numIncidences;
    }

}
