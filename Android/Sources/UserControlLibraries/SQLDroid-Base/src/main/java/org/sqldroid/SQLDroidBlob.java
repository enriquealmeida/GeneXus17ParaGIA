package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class SQLDroidBlob implements Blob {

	byte [] b;
	
	public SQLDroidBlob(byte [] b) {
		this.b = b;
	}
	
	@Override
	public InputStream getBinaryStream() throws SQLException {
	        InputStream input = new ByteArrayInputStream(b);
                return input;
	}

	@Override
	public byte[] getBytes(long pos, int length) throws SQLException {
		return b;
	}

	@Override
	public long length() throws SQLException {
		
		return b.length;
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int setBytes(long pos, byte[] theBytes) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public int setBytes(long pos, byte[] theBytes, int offset, int len)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public void truncate(long len) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void free() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getBinaryStream(long pos, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


}
