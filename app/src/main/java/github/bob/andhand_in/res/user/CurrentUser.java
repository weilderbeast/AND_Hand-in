package github.bob.andhand_in.res.user;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CurrentUser {
    private static User current_user;
    private static Lock lock = new ReentrantLock();
    private static CurrentUser currentUser;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (currentUser == null)
            synchronized (lock) {
                if (currentUser == null)
                    currentUser = new CurrentUser();
            }

        return currentUser;
    }

    public void setUser(User user) {
        current_user = user;
    }

    public User getUser() {
        return current_user;
    }
}
