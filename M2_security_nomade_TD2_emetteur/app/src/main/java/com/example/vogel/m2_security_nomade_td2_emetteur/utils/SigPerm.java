package com.example.vogel.m2_security_nomade_td2_emetteur.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;


public class SigPerm {
    /**
     * Teste la validité d'une permission custom. Pour être valide, une permission doit être
     * de niveau SIGNATURE et que l'app qui l'a définie aie la signature attendur
     * @param ctx contexte applicatif (si appelé depuis une activité, passet this)
     * @param sigPermName nom de la permission à tester
     * @param correctHash signature attendue de l'app ayant défini la permission
     * @return true si la permission est valide, false sinon.
     */
    public static boolean test(Context ctx, String sigPermName, String correctHash) {
        if (correctHash == null) return false;
        correctHash = correctHash.replaceAll(" ", "");
        return correctHash.equals(hash(ctx, sigPermName));
    }
    public static String hash(Context ctx, String sigPermName) {
        if (sigPermName == null) return null;
        try {
// Get the package name of the application which declares a permission named sigPermName.
            PackageManager pm = ctx.getPackageManager();
            PermissionInfo pi;
            pi = pm.getPermissionInfo(sigPermName, PackageManager.GET_META_DATA);
            String pkgname = pi.packageName;
// Fail if the permission named sigPermName is not a Signature Permission
            if (pi.protectionLevel != PermissionInfo.PROTECTION_SIGNATURE) return null;
// Return the certificate hash value of the application which declares a permission named sigPermName.
            return PkgCert.hash(ctx, pkgname);
        } catch (NameNotFoundException e) {
            return null;
        }
    }
}