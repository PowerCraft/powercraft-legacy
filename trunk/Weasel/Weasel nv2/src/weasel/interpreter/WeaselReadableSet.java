package weasel.interpreter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WeaselReadableSet<T> implements Set<T> {

	private final List<T> list;
	
	public WeaselReadableSet(List<T> list) {
		this.list = list;
	}

	@Override
	public boolean add(T e) {
		throw new WeaselNativeException("add not suported");
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new WeaselNativeException("addAll not suported");
	}

	@Override
	public void clear() {
		throw new WeaselNativeException("clear not suported");
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		throw new WeaselNativeException("remove not suported");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new WeaselNativeException("removeAll not suported");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new WeaselNativeException("retainAll not suported");
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <V> V[] toArray(V[] a) {
		return list.toArray(a);
	}
	
}
