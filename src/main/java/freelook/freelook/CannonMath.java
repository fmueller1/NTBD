package mathTest.MathTest;

public class CannonMath {
    final double initialOffsetX = 0;
    final double initialOffsetY = 0.25;
    final double initialOffsetZ = 0;
    final double surfaceOffset = -0.25;
    final double distanceFromSurface = 0.375;
    //non-final variables are defined in the constructor as their values depend on non constants.
    double sign = 0;
    double finalXOffset = 0;
    double doRotation;

    public CannonMath(){
        if(distanceFromSurface > 0) sign=1;
        if(distanceFromSurface < 0) sign=-1;

        finalXOffset = distanceFromSurface + sign*surfaceOffset + initialOffsetX;
    }

    private double getFinalZOffset(double cameraYaw){
        return distanceFromSurface*Math.cos(cameraYaw) + initialOffsetY;
    }

    private double getDoRotation(double cameraYaw){
        if(cameraYaw <= -Math.PI/2) return -1.0;
        if(cameraYaw >= Math.PI/2) return 1.0;
        return 0.0;
    }

    public double getNewCameraYaw(double cameraPitch, double cameraYaw){
        doRotation = getDoRotation(cameraYaw);
        double theta = Math.atan((finalXOffset*Math.tan(cameraYaw) - initialOffsetZ)/(distanceFromSurface));
        theta = Math.toDegrees(theta);
        theta += 180d*doRotation;
        theta = Math.toRadians(theta);
        return theta;
    }

    public double getNewCameraPitch(double cameraPitch, double cameraYaw){
        double theta = finalXOffset*Math.tan(cameraPitch);
        theta *= Math.abs(1d/Math.cos(cameraYaw));
        theta -= initialOffsetY;
        double denominator = distanceFromSurface * Math.abs(1d/Math.cos(getNewCameraYaw(cameraPitch, cameraYaw)));
        theta /= denominator;
        System.out.println(theta);
        theta = Math.atan(theta);
        return theta;
    }
}