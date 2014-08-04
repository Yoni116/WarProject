import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ObjectFilter implements Filter {
    private Object o;

    public ObjectFilter(Object o) {
	this.o = o;
    }

    @Override
    public boolean isLoggable(LogRecord rec) {
	if (rec.getParameters() != null) {
	    Object temp = rec.getParameters()[0];
	    return o == temp;
	} else
	    return false;
    }

}
