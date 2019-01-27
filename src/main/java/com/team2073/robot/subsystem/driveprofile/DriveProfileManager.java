package com.team2073.robot.subsystem.driveprofile;

import java.util.ArrayList;

public class DriveProfileManager {

    private ArrayList<DriveProfile> driveProfiles = new ArrayList<>();

   public void registerProfile(DriveProfile driveProfile) {
        System.out.println("Registering profile " + driveProfile.toString());
        driveProfiles.add(driveProfile);
    }

    public DriveProfile getDriveProfile(int index) {
        return driveProfiles.get(index);
    }

    public ArrayList<DriveProfile> getDriveProfiles() {
        return driveProfiles;
    }
}
