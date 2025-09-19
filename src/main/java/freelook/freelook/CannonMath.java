package freelook.freelook;

public class CannonMath {
    final double initialOffsetY = 0.25;
    final double initialOffsetX = 0;
    final double initialOffsetZ = 0;
    final double surfaceOffset = -0.25;
    final double distanceFromSurface = 0.375;
    //non-final variables are defined in the constructor as their values depend on non constants.
    double sign = 0;
    double finalXOffset = 0;
    double finalZOffset = getFinalZOffset(0);
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

        finalXOffset = distanceFromSurface + sign*surfaceOffset + initialOffsetX;
    }

    private double getFinalZOffset(double cameraYaw){
        return distanceFromSurface*Math.cos(cameraYaw) + initialOffsetY;
    }

    public double getNewCameraYaw(double cameraPitch, double cameraYaw){
        finalZOffset = getFinalZOffset(cameraYaw);
        double theta = Math.atan((finalXOffset*Math.tan(cameraYaw) - initialOffsetZ)/(distanceFromSurface));
        theta += 180*doRotation;
        return theta;
    }

    public double getNewCameraPitch(double cameraPitch, double cameraYaw){
        finalXOffset = getFinalZOffset(cameraYaw);
        double theta = finalXOffset*Math.tan(cameraPitch);
        theta *= Math.abs(1/Math.cos(getNewCameraYaw(cameraPitch, cameraYaw)));
        theta -= initialOffsetZ;
        double denominator = finalXOffset * Math.abs(1/Math.cos(getNewCameraYaw(cameraPitch, cameraYaw)));
        theta /= denominator;
        theta = Math.atan(theta);
        return theta;
    }
}
