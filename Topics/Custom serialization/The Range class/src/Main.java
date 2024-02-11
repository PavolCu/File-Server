import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


class Range implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;


	private final int from;

	private final int to;


	public Range(int from, int to) {
		if (from > to) {
			throw new IllegalArgumentException("Start is greater than end");
		}
		this.from = from;
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Serial
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (from > to) {
			throw new IllegalArgumentException("Start is greater than end");
		}
	}

	@Override
	public String toString() {
		return "Range{" +
				"from=" + from +
				", to=" + to +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Range range = (Range) o;
		return from == range.from && to == range.to;
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to);
	}
}