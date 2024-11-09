
package com.codestudioapps.fitnessApp.WalkandStep;

import android.content.pm.PackageManager;

import com.codestudioapps.fitnessApp.WalkandStep.services.AbstractStepDetectorService;
import com.codestudioapps.fitnessApp.WalkandStep.services.AccelerometerStepDetectorService;
import com.codestudioapps.fitnessApp.WalkandStep.services.HardwareStepDetectorService;
import com.codestudioapps.fitnessApp.WalkandStep.utils.AndroidVersionHelper;




public class Factory {



    public static Class<? extends AbstractStepDetectorService> getStepDetectorServiceClass(PackageManager pm){
        if(pm != null && AndroidVersionHelper.supportsStepDetector(pm)) {
            return HardwareStepDetectorService.class;
        }else{
            return AccelerometerStepDetectorService.class;
        }
    }
}
