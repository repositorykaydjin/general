package com.example.vogel.m2_security_nomade_td2_emetteur.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PkgCert {

    /**
     * Teste l'authenticité d'une app à partir du hash de sa signature
     * @param ctx contexte applicatif
     * @param pkgname Nom du package de l'app à tester
     * @param correctHash Hash de la signature attendue
     * @return true si la signature de l'app installée correspond bien
     */
    public static boolean test(Context ctx, String pkgname, String correctHash) {
        if (correctHash == null) return false;
        correctHash = correctHash.replaceAll(" ", "");
        return correctHash.equals(hash(ctx, pkgname));
    }

    /**
     * Récupère le hash de la signature d'une app installée
     * @param ctx contexte applicatif
     * @param pkgname nom du package de l'app dont on veut récupérer le hash
     * @return le hash, ou null si l'app n'est pas présente.
     */
    public static String hash(Context ctx, String pkgname) {
        if (pkgname == null) return null;
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pkginfo = pm.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
            if (pkginfo.signatures.length != 1) return null;
// Will not handle multiple signatures.
            Signature sig = pkginfo.signatures[0];
            byte[] cert = sig.toByteArray();
            byte[] sha256 = computeSha256(cert);
            return byte2hex(sha256);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Fonction de hashage (en SHA-256)
     * @param data données à hasher
     * @return le hash
     */
    private static byte[] computeSha256(byte[] data) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(data);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    private static String byte2hex(byte[] data) {
        if (data == null) return null;
        final StringBuilder hexadecimal = new StringBuilder();
        for (final byte b : data) {
            hexadecimal.append(String.format("%02X", b));
        }
        return hexadecimal.toString();
    }
}