#!/usr/bin/env groovy
import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

if (args.length < 2)
{
    println "USAGE: read-manifest.groovy <jar_filename> <property_name>"
    System.exit(-1)
}

var jar = args[0]
var prop = args[1]

var realProp = new Attributes.Name(prop)

JarFile jarFile = new JarFile(jar)
var manifest = jarFile.manifest.getMainAttributes()

if(manifest.containsKey(realProp)) {
    var val = manifest.getValue(prop)
    println("prop=$val")
}