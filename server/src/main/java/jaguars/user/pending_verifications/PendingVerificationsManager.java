package jaguars.user.pending_verifications;

import jaguars.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PendingVerificationsManager {
    @Autowired
    PendingVerificationsRepository pvr;

    public PendingVerifications findUserById(int userId) {
        return pvr.findOne(userId);
    }

    public List<PendingVerifications> findUsersByUsername(String username) {
        ArrayList<PendingVerifications> userList = (ArrayList<PendingVerifications>) pvr.findByUsername(username);
        return userList;
    }

    public List<PendingVerifications> findUsersByKey(String verify) {
        ArrayList<PendingVerifications> userList = (ArrayList<PendingVerifications>) pvr.findByVerify(verify);
        return userList;
    }

    public void removePendingVerification(int id){
        pvr.delete(id);
    }

    public void addPendingVerification(String username, String verify){
        PendingVerifications pv = new PendingVerifications(username, verify);
        pvr.save(pv);
    }
}
