import java.util.concurrent.locks.ReentrantLock;

public class Tile {
    private Object occupant;
    private final ReentrantLock lock = new ReentrantLock();

    public Tile() {
        this.occupant = null;
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    public Object getOccupant() {
        return occupant;
    }

    public void setOccupant(Object occupant) {
        this.occupant = occupant;
    }

    public void lock() {
         lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean tryLock() {
        return(lock.tryLock());
    }

    @Override
    public String toString() {
        return occupant == null ? " " : occupant.toString();
    }
}
