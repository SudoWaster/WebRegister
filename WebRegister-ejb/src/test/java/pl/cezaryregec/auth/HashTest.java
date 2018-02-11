package pl.cezaryregec.auth;

import com.google.inject.Inject;
import java.util.Random;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.cezaryregec.DefaultModule;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RunWith(JukitoRunner.class)
@UseModules(DefaultModule.class)
public class HashTest {
    
    public static final int MIN_LENGTH = 64;
    public static final int MAX_LENGTH = 1024;
    public static final int STRINGS = 16;
    
    private final Random randomGenerator = new Random();
    
    private byte[] testBytes;
    private byte[] controlBytes;
    
    private String[] testStrings;
    private String[] controlStrings;
    
    @Inject
    HashGenerator hashGenerator;
    
    private void setupBytes() {
        testBytes = generateBytes();
        controlBytes = generateBytes();
    }    
        
    private void setupStrings() {    
        testStrings = new String[STRINGS];
        controlStrings = new String[STRINGS];
        
        for(int i = 0; i < STRINGS; i++) {
            testStrings[i] = generateString();
            controlStrings[i] = generateString();
        }
    }
    
    private byte[] generateBytes() {
        int length = Math.abs(randomGenerator.nextInt()) % MAX_LENGTH + MIN_LENGTH;
        byte[] bytes = new byte[length];
        
        randomGenerator.nextBytes(bytes);
        
        System.out.println("Generated " + length + " bytes of data");
        
        return bytes;
    }
    
    private String generateString() {
        int length = Math.abs(randomGenerator.nextInt()) % MAX_LENGTH + MIN_LENGTH;
        
        String string = "";
        
        for(int i = 0; i < length; i++) {
            string += (char) (Math.abs(randomGenerator.nextInt()) % Integer.MAX_VALUE);
        }
        
        System.out.println("Generated String of length " + length);
        
        return string;
    }
    
    
    @Test
    public void byteHashesComparisonTest() {
        setupBytes();
        
        System.out.println("Comparing byte hashes");
        
        byte[] testHash = hashGenerator.generateHash(testBytes);
        byte[] controlHash = hashGenerator.generateHash(controlBytes);
        
        Assert.assertThat(testBytes, IsNot.not(IsEqual.equalTo(controlBytes)));
        Assert.assertThat(testHash, IsNot.not(IsEqual.equalTo(testBytes)));
        Assert.assertArrayEquals(hashGenerator.generateHash(testBytes), testHash);
        Assert.assertThat(controlHash, IsNot.not(IsEqual.equalTo(testHash)));
    }
    
    @Test
    public void stringHashComparisonTest() {
        setupStrings();
        
        System.out.println("Comparing String hashes");
        
        System.out.println(testStrings[0] != null);
        System.out.println(hashGenerator != null);
        String testHash = hashGenerator.generateHash(testStrings[0]);
        String controlHash = hashGenerator.generateHash(controlStrings[0]);
        
        Assert.assertNotEquals(testStrings[0], controlStrings[0]);
        Assert.assertNotEquals(testHash, testStrings[0]);
        Assert.assertEquals(hashGenerator.generateHash(testStrings[0]), testHash);
        Assert.assertNotEquals(controlHash, testHash);
    }
    
    @Test
    public void stringArrayHashComparisonTest() {
        setupStrings();
        
        System.out.println("Comparing String arrays hashes");
        
        String testHash = hashGenerator.generateHash(testStrings);
        String controlHash = hashGenerator.generateHash(controlStrings);
        
        Assert.assertThat(testStrings, IsNot.not(IsEqual.equalTo(controlStrings)));
        Assert.assertNotEquals(testHash, testStrings);
        Assert.assertEquals(hashGenerator.generateHash(testStrings), testHash);
        Assert.assertThat(controlHash, IsNot.not(IsEqual.equalTo(testHash)));
    }
    
    @Test
    public void saltHashTest() {
        setupStrings();
        
        System.out.println("Comparing String salt hashes");
        
        String testHash = hashGenerator.generateSaltHash(testStrings[0], testStrings[1]);
        String controlHash = hashGenerator.generateSaltHash(controlStrings[0], controlStrings[1]);
        
        Assert.assertNotEquals(testHash, controlHash);
        Assert.assertEquals(hashGenerator.generateSaltHash(testStrings[0], testStrings[1]), testHash);
    }
    
}
