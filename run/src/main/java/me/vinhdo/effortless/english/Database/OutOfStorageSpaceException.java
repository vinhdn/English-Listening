package me.vinhdo.effortless.english.Database;

public class OutOfStorageSpaceException extends StorageUnavailableException {
	public OutOfStorageSpaceException(String description) {
		super(description, "");
	}
}
