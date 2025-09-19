package freelook.freelook;

public class CannonMath {
    double initialOffsetY = 0;
    double initialOffsetX = 0;
    double initialOffsetZ = 0.25;
    double surfaceOffset = 0.25;
    double distanceFromSurface = 0.375;
    double sign = 0;
    double finalXOffset = getFinalXOffset(0);
    double finalZOffset = distanceFromSurface + sign*initialOffsetX + surfaceOffset;
    double doRotation;
    public CannonMath(){
        if(distanceFromSurface > 0) sign=1;
        if(distanceFromSurface < 0) sign=-1;

        if(finalXOffset > 0) doRotation=0;
        if(finalXOffset < 0) doRotation=1;
        if(finalXOffset == 0) {
            if(finalZOffset > 0) doRotation=0;
            if(finalZOffset < 0) doRotation=1;
        }
    }

    private double getFinalXOffset(double cameraYaw){
        return distanceFromSurface*Math.cos(cameraYaw) + initialOffsetY;
    }

    public double getCameraYaw(double cameraPitch, double cameraYaw){
        finalXOffset = getFinalXOffset(cameraYaw);
        double theta = 0;
        return theta;
    }

    public double getCameraPitch(double cameraPitch, double cameraYaw){
        finalXOffset = getFinalXOffset(cameraYaw);
        double theta = 0;
        return theta;
    }
}
