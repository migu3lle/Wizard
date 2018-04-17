package aau.losamigos.wizard.elements;

import aau.losamigos.wizard.base.Hand;

/**
 * Created by flo on 10.04.2018.
 */

public class Player {

    private static int latestID = 0;

    private int id;

    private String name;

    private int points;

    private int actualStiches;

    private int calledStiches;

    public Player(int id, String name) {
        this.id = getNextId();
        if(name == null) {
            name = "" + id;
        }
        this.name = name;
        this.points = 0;
        this.actualStiches =0;
    }

    private static int getNextId(){
        return ++latestID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void decreasePoints(int points) {
        if(points > this.points)
            points = this.points;
        this.points -= points;
    }

    public void increaseStiches() {
        this.actualStiches++;
    }

    public void restStiches() {
        this.actualStiches = 0;
    }

    public int getActualStiches() {
        return this.actualStiches;
    }

    public void setCalledStiches(int stiches) {
        this.calledStiches = stiches;
    }

    public int getCalledStiches() {
        return this.calledStiches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
