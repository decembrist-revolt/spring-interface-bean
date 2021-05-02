**Spring Interface Bean Definition**  
Library gives an ability to make beans without stereotype annotations (@Service @Component etc.) but through interface inheritance (Like spring data repositories do)

**Getting started**  
_Maven:_  

    <dependency>
        <groupId>org.decembrist.spring</groupId>
        <artifactId>spring-interface-bean</artifactId>
        <version>1.1.0</version>
    </dependency>
_Gradle:_  

    implementation "org.decembrist.spring:spring-interface-bean:1.1.0"
_Tested spring-boot version: **2.4.5**_  
_Example:_  

    //1. Extend class with IBean interface to make it singleton bean
    class SomeClass implements IBean {}
    
    class AnotherClass {
        //2. SomeClass above will be injected (Singleton scope)
        @Autowired private SomeClass someClass;
    }
    
    //3. SubInterface works the same way
    interface InterfaceBeanSubInterface extends IBean {
    }
    
    class SomeClass2 implements InterfaceBeanSubInterface {}
    
    class AnotherClass2 {
        //4. SomeClass2 above will be injected (Singleton scope)
        @Autowired private SomeClass2 someClass;
    }
_To use without springboot autoconfiguration:_

    //import postprocessor
    @Import(InterfaceBeanPostProcessor.class)
_Manual interface bean functionality:_  
If you don't want to use another dependency just copy this bean to your codebase [InterfaceBeanPostProcessor](https://github.com/decembrist-revolt/spring-interface-bean/blob/master/src/main/java/org/decembrist/spring/interfacebean/InterfaceBeanPostProcessor.java)

    //And replace IBean.class whatever interface you want to be bean definer
    scanner.addIncludeFilter(new AssignableTypeFilter(IBean.class));
_Exceptions:_  
1. If your bean class has one of stereotype annotations - everything works as usual, interface bean postprocessor ignores that classes
2. Only singleton scope supported. Now you can't change interface beans scope, you should use stereotype annotations in this case

_Properties:_

    #disable interface bean definition
    spring.interface-bean=false