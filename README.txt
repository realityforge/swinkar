= swinkar

This is project for testing out swing in an OSGi context.

== iPojo: The strange/annoying things

* jmx extender uses an annotation @Property with the same name as configuration annotation
  forcing you to fully qualify one. Why not just use @JmxProperty instead?  
* EventAdmin extender has an annotation with the exact same name as the class (i.e. @Publisher)
  so you need to fully qualify one. Why not just name it @PublisheEvent instead?
* @Subscriber and @Publisher annotations use non javaish name data_key and data_type