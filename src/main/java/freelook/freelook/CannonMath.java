package freelook.freelook;

import org.joml.Vector2d;

public class CannonMath {
    final int cannonRot = 1;
    final double initialOffsetX = 0;
    final double initialOffsetY = 0.25 + 0.02525;
    final double initialOffsetZ = 0;
    final double surfaceOffset = -0.2568;
    final double distanceFromSurface = 0.375;
    final double pitchScaleConstant = 0.1869;
    //non-final variables are defined in the constructor as their values depend on the previously constants.
    double sign = 0;
    double finalXOffset = 0;
    double doRotation;

    public CannonMath(){
        if(distanceFromSurface > 0) sign=1;
        if(distanceFromSurface < 0) sign=-1;

        finalXOffset = distanceFromSurface + sign*surfaceOffset + initialOffsetX;
    }

    private double getDoRotation(double cameraYaw){
        if(cameraYaw <= -Math.PI/2.0) return -1.0;
        if(cameraYaw > Math.PI/2.0) return 1.0;
        return 0.0;
    }

    private double getRotatedYaw(double cameraYaw){
        cameraYaw += Math.PI*cannonRot/2.0;
        return cameraYaw;
    }

    private double undoYawRotation(double cameraYaw){
        cameraYaw -= Math.PI*cannonRot/2.0;
        return cameraYaw;
    }


    public double getNewCameraYaw(double cameraYaw){
        doRotation = getDoRotation(cameraYaw);
        double theta = Math.atan((finalXOffset*Math.tan(cameraYaw) - initialOffsetZ)/(distanceFromSurface));
        theta = clamp(theta);
        return theta;
    }

    public double getNewCameraPitch(double cameraPitch, double cameraYaw){
        double theta = finalXOffset*Math.tan(cameraPitch);
        theta *= Math.abs(1.0/Math.cos(cameraPitch));
        theta -= initialOffsetY;
        double denominator = Math.abs(1.0/Math.cos(getNewCameraYaw(cameraYaw)));
        denominator *= distanceFromSurface;
        theta /= denominator;
        theta = Math.atan(theta);
        return clamp(theta);
    }

    private double clamp(double x){
        return (x+Math.PI)%(2*Math.PI)-Math.PI;
    }

    public Vector2d getNewCameraDirection(double cameraYaw, double cameraPitch){
        cameraYaw = clamp(Math.toRadians(cameraYaw));
        cameraPitch = clamp(-Math.toRadians(cameraPitch * pitchScaleConstant));
        Vector2d vec = new Vector2d();
        double rotatedYaw = getRotatedYaw(cameraYaw);
        vec.x = clamp(undoYawRotation(getNewCameraYaw(rotatedYaw)) + Math.PI*doRotation);
        vec.y = getNewCameraPitch(cameraPitch, rotatedYaw);
        vec.x = Math.toDegrees(vec.x);
        vec.y = -Math.toDegrees(vec.y);
        return vec;
    }
}