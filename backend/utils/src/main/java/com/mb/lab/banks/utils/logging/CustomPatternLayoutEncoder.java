package com.mb.lab.banks.utils.logging;

import java.nio.charset.Charset;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import ch.qos.logback.core.util.OptionHelper;

public class CustomPatternLayoutEncoder extends EncoderBase<ILoggingEvent> {

	private String pattern = "%pLowercase|%d{yyyy/MM/dd HH:mm:ss:SSS}|%logger|%m%n%wEx";
	private String errorLogPattern = "error|%d{yyyy/MM/dd HH:mm:ss:SSS}|%logger|%m|%mdc{functionArgs:-null}|%mdc{errorCode:-null}%n%wEx";
	private String businessLogPattern = "%marker|%mdc{applicationName:-null}|%d{yyyy/MM/dd HH:mm:ss:SSS}|%mdc{username:-null}|%mdc{remoteIp:-null}|%mdc{requestedPath:-null}|%mdc{functionName:-null}|%mdc{functionArgs:-null}|%mdc{functionClass:-null}|%mdc{duration:-null}|%m%n%wEx";

	private PatternLayout defaultLayout;
	private PatternLayout errorLogLayout;
	private PatternLayout businessLogLayout;

	/**
	 * The charset to use when converting a String into bytes.
	 * <p/>
	 * By default this property has the value <code>null</null> which corresponds to
	 * the system's default charset.
	 */
	private Charset charset;

	public Charset getCharset() {
		return charset;
	}

	/**
	 * Set the charset to use when converting the string returned by the layout into
	 * bytes.
	 * <p/>
	 * By default this property has the value <code>null</null> which corresponds to
	 * the system's default charset.
	 *
	 * @param charset
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public byte[] headerBytes() {
		return null;
	}

	@Override
	public byte[] footerBytes() {
		return null;
	}

	@Override
	public byte[] encode(ILoggingEvent event) {
		String txt = getLayout(event).doLayout(event);
		return convertToBytes(txt);
	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public void start() {
		PatternLayout defaultLayout = new PatternLayout();
		defaultLayout.setContext(context);
		defaultLayout.setPattern(OptionHelper.substVars(pattern, context));
		defaultLayout.setOutputPatternAsHeader(false);
		defaultLayout.start();
		this.defaultLayout = defaultLayout;

		PatternLayout errorLogLayout = new PatternLayout();
		errorLogLayout.setContext(context);
		errorLogLayout.setPattern(OptionHelper.substVars(errorLogPattern, context));
		errorLogLayout.setOutputPatternAsHeader(false);
		errorLogLayout.start();
		this.errorLogLayout = errorLogLayout;

		PatternLayout businessLogLayout = new PatternLayout();
		businessLogLayout.setContext(context);
		businessLogLayout.setPattern(OptionHelper.substVars(businessLogPattern, context));
		businessLogLayout.setOutputPatternAsHeader(false);
		businessLogLayout.start();
		this.businessLogLayout = businessLogLayout;

		started = true;
	}

	@Override
	public void stop() {
		if (this.defaultLayout != null) {
			this.defaultLayout.stop();
		}
		if (this.errorLogLayout != null) {
			this.errorLogLayout.stop();
		}
		if (this.businessLogLayout != null) {
			this.businessLogLayout.stop();
		}
		started = false;
	}

	private PatternLayout getLayout(ILoggingEvent event) {
		if (event.getMarker() == null) {
			if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
				return errorLogLayout;
			}
			return defaultLayout;
		}

		return businessLogLayout;
	}

	private byte[] convertToBytes(String s) {
		if (charset == null) {
			return s.getBytes();
		} else {
			return s.getBytes(charset);
		}
	}

}
