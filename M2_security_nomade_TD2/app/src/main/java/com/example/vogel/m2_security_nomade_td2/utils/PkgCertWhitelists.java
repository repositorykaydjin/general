package com.example.vogel.m2_security_nomade_td2.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class PkgCertWhitelists {
    private Map<String, String> mWhitelists = new HashMap<String, String>();

    /**
     * Ajouter une app et sa signature à la liste blanche
     * @param pkgname Nom de l'app
     * @param sha256 signature de l'app
     */
    public boolean add(String pkgname, String sha256) {
        if (pkgname == null) return false;
        if (sha256 == null) return false;
        sha256 = sha256.replaceAll(" ", "");
        if (sha256.length() != 64) return false;
// SHA-256 -> 32 bytes -> 64 chars
        sha256 = sha256.toUpperCase();
        if (sha256.replaceAll("[0-9A-F]+", "").length() != 0) return false; // found non hex char
        mWhitelists.put(pkgname, sha256);
        return true;
    }

    /**
     * Teste si une app installée est sur la liste et présente la bonne signature
     * @param ctx contexte applicatif (si appelé depuis dans une activité, passer this)
     * @param pkgname nom du package de l'app à tester
     * @return true si l'app est sur la liste et correctement signée
     */
    public boolean test(Context ctx, String pkgname) {
// Get the correct hash value which corresponds to pkgname.
        String correctHash = mWhitelists.get(pkgname);
// Compare the actual hash value of pkgname with the correct hash value.
        return PkgCert.test(ctx, pkgname, correctHash);
    }
}