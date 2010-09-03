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
* Provides have to be parent classes or interfaces not the class itself ... seems like an arbitrary
  decision???
* ipojoization futzes with super calls - breaks scenario where JPanel passes parameter to superclass.
  The bytecode wont verify as extra parameter passed to ctor.
* Why can I not set static service properties that do not need to be mirrored in fields? This could
  be done as an annotation on the class.
* Should be able to restrict ServiceProperty to a particular service
* Why not have a single annotation at class level that can define multiple services with possibly
  multiple properties, controls etc. Maybe define a class level @Provides such as

@Provides{
{
  @Service{type=Foo.class, properties ={@ServiceProperty{name="x", value="1"}, control = "myControlVaR"},
  @Service{type=Bar.class, properties ={@ServiceProperty{name="x", value="2"}},
}
)

== TODO

* Investigate the substance themes as possible base for skinning / branding app.
  https://substance.dev.java.net/see.html
* Consider using FEST to perform GUI tests
  See http://docs.codehaus.org/display/FEST/Writing+EDT-safe+GUI+tests
* Tests fail if non EDT access to repaint manager during testing.
  See http://docs.codehaus.org/display/FEST/Testing+that+access+to+Swing+components+is+done+in+the+EDT
