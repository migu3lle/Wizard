package aau.losamigos.wizard.elements;

/**
 * Created by flo on 10.04.2018.
 */

public class Player {

    private int id;

    private String name;

    private int points;

    public Player(int id, String name) {
        this.id = id;
        if(name == null) {
            name = "" + id;
        }
        this.name = name;
        this.points = 0;
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
