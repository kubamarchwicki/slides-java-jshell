public class Foo {
  	public final String text;
       
   	public Foo(String s) {
   		this.text = s;
   	}
}

public class Wrapper {
	public Wrapper(Foo foo) {
		System.out.println(foo.text);
	}
}

import org.springframework.context.support.*
import org.springframework.context.annotation.*
GenericApplicationContext ctx = new AnnotationConfigApplicationContext()

ctx.registerBean(Foo.class, () -> new Foo("bar"))
ctx.registerBean(Wrapper.class, bd -> bd.setScope("prototype"))
ctx.refresh()
ctx.getBean(Foo.class)
ctx.getBean(Wrapper.class)
