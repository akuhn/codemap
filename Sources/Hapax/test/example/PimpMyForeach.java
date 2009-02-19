package example;

import static ch.akuhn.util.Each.withIndex;
import static ch.akuhn.util.Out.puts;
import static ch.akuhn.util.Strings.letters;
import static ch.akuhn.util.query.Query.$result;
import static ch.akuhn.util.query.Query.groupedBy;
import static ch.akuhn.util.query.Query.reject;
import ch.akuhn.util.As;
import ch.akuhn.util.Each;
import ch.akuhn.util.IteratorAsList;
import ch.akuhn.util.query.GroupedBy;
import ch.akuhn.util.query.Reject;

public class PimpMyForeach {

    public static void main(String[] args) {
        
        Iterable<String> words = As.list(letters("Yo dawg, I herd you liek looping so we put a foreach loop in your loop so you can loop while you loop"));
        
        for (Reject<String> each: reject(words)) {
            each.yield = each.element.length() % 2 == 1;
        }
        puts( $result() );
        
        for (GroupedBy<String> each: groupedBy(words)) {
            each.yield = each.element.length();
        }
        puts( $result() );
        
        for (Each<String> each: withIndex(words)) {
            puts( "%4d\t%s", each.index, each.element);
        }
        
        IteratorAsList<String> $$$ = new IteratorAsList<String>(letters("Yo dawg, I herd you liek looping so we put a foreach loop in your loop so you can loop while you loop"));
        
        System.out.println( $$$ );
        System.out.println( $$$.get(4) );
        System.out.println( $$$ );
        System.out.println( $$$.contains("put") );
        System.out.println( $$$ );
        System.out.println( $$$.size() );
        System.out.println( $$$.upTo(1000) );
        System.out.println( $$$ );
        System.out.println( $$$ );
        
        
        
    }
    
}
