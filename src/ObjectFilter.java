import java.util.logging.Filter;
import java.util.logging.LogRecord;


public class ObjectFilter implements Filter {
	private Object o;

	public ObjectFilter(Object o) {
		this.o=o;
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		if(record.getParameters()[0]==o)
			return true;
		return false;
	}

}
