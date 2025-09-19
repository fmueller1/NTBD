package freelook.freelook;

public class CannonMath {
    final double initialOffsetY = 0;
    final double initialOffsetX = 0;
    final double initialOffsetZ = 0.25;
    final double surfaceOffset = 0.25;
    final double distanceFromSurface = 0.375;
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

    public double getNewCameraYaw(double cameraPitch, double cameraYaw){
        finalXOffset = getFinalXOffset(degreeToRad(cameraYaw));
        double theta = 0;
        theta = Math.atan((finalXOffset*Math.tan(degreeToRad(cameraYaw)) - initialOffsetZ)/(distanceFromSurface));
        theta = radToDegree(theta) + 180*doRotation;
        return theta;
    }

    public double getNewCameraPitch(double cameraPitch, double cameraYaw){
        finalXOffset = getFinalXOffset(degreeToRad(cameraYaw));
        double theta = 0;
        theta = finalZOffset*Math.tan(degreeToRad(cameraPitch));
        theta *= Math.abs(1/Math.cos(degreeToRad(cameraYaw)));
        theta -= initialOffsetZ;
        double denominator = distanceFromSurface * Math.abs(1/Math.cos(degreeToRad(getNewCameraYaw(cameraPitch, cameraYaw))));
        theta /= denominator;
        theta = Math.atan(theta);
        return radToDegree(theta);
    }

    private double degreeToRad(double num){
        return num / Math.PI * 180;
    }

    private  double radToDegree(double num){
        return num * Math.PI / 180;
    }
}
