= swinkar

This is project for testing out swing in an OSGi context.

== iPojo: The strange/annoying things

* jmx extender uses an annotation @Property with the same name as configuration annotation
  forcing you to fully qualify one. Why not just use @JmxProperty instead?  
* EventAdmin extender has an annotation with the exact same name as the class (i.e. @Publisher)
  so you need to fully qualify one. Why not just name it @PublisheEvent instead?
* Non-javaish names in attributes such as;
  - @Subscriber.data_key
  - @Subscriber.data_type
  - @Publisher.data_key
  - @Publisher.data_type
  - @Component.factory_method
* @Updated annotated methods should not need to have Dictionary parameter - should be optional.
  Regardless the syntax should be checked at ipojoizer time and fail then if not valid.
