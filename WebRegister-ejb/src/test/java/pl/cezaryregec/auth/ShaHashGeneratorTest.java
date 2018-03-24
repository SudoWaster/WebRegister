package pl.cezaryregec.auth;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import pl.cezaryregec.Config;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class ShaHashGeneratorTest {
    
    @Mock
    Config config;
    
    @InjectMocks
    ShaHashGenerator hashGenerator = new ShaHashGenerator();
    
    @Test
    public void shouldEnodeBytesToString() {
        byte[] input = {-72, 41, -74, -31, 18, 35, -78, 44, -6, 15, 75, -110, 92, 25, -41, 103, 109, -27, -111, 10, 75, -86, 11, -107, 82, -55, 105, 23, 113, -48, -3, -120};
        
        String expectedString = "b829b6e11223b22cfa0f4b925c19d7676de5910a4baa0b9552c9691771d0fd88";
        String encodedString = hashGenerator.encodeToString(input);
        
        Assert.assertThat(encodedString, Is.is(IsEqual.equalTo(expectedString)));
    }
    
    @Test
    public void shouldGenerateSha256HashBytes() {
        byte[] input = {76, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109, 32, 100, 111, 108, 111, 114, 32, 115, 105, 116, 32, 97, 109, 101, 116, 44, 32, 99, 111, 110, 115, 101, 99, 116, 101, 116, 117, 114, 32, 97, 100, 105, 112, 105, 115, 105, 99, 105, 110, 103, 32, 101, 108, 105, 116, 44, 32, 115, 101, 100, 32, 100, 111, 32, 101, 105, 117, 115, 109, 111, 100, 32, 116, 101, 109, 112, 111, 114, 32, 105, 110, 99, 105, 100, 105, 100, 117, 110, 116, 32, 117, 116, 32, 108, 97, 98, 111, 114, 101, 32, 101, 116, 32, 100, 111, 108, 111, 114, 101, 32, 109, 97, 103, 110, 97, 32, 97, 108, 105, 113, 117, 97, 46};
        
        byte[] expectedBytes = {-72, 41, -74, -31, 18, 35, -78, 44, -6, 15, 75, -110, 92, 25, -41, 103, 109, -27, -111, 10, 75, -86, 11, -107, 82, -55, 105, 23, 113, -48, -3, -120};
        byte[] encodedBytes = hashGenerator.generateHash(input);
        
        Assert.assertThat(encodedBytes, Is.is(IsEqual.equalTo(expectedBytes)));
    }
    
    @Test
    public void shouldGenerateSha256HashString() {
        String input = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        
        String expectedHash = "b829b6e11223b22cfa0f4b925c19d7676de5910a4baa0b9552c9691771d0fd88";
        String generatedHash = hashGenerator.generateHash(input);
        
        Assert.assertThat(generatedHash, Is.is(IsEqual.equalTo(expectedHash)));
    }
    
    @Test
    public void shouldGenerateSha256HashStringFromArray() {
        String[] input = {"Lorem", " ", "ipsum", " ", "dolor", " ", "sit", " ", "amet", ",", " ", "consectetur", " ", "adipisicing", " ", "elit", ",", " ", "sed", " ", "do", " ", "eiusmod", " ", "tempor", " ", "incididunt", " ", "ut", " ", "labore", " ", "et", " ", "dolore", " ", "magna", " ", "aliqua", "."};
        
        String expectedHash = "b829b6e11223b22cfa0f4b925c19d7676de5910a4baa0b9552c9691771d0fd88";
        String generatedHash = hashGenerator.generateHash(input);
        
        Assert.assertThat(generatedHash, Is.is(IsEqual.equalTo(expectedHash)));
    }
    
    @Test
    public void shouldGenerateSha256SaltHash() {
        String input = "Lorem ipsum";
        String salt = "dolor";
        
        String expectedHash = "ce221d0fbe0c6a632979e758ea94b0cfdb1327242566708b8ff9e43eed5e11c0";
        String generatedHash = hashGenerator.generateSaltHash(input, salt);
        
        Assert.assertThat(generatedHash, Is.is(IsEqual.equalTo(expectedHash)));
    }
    
    @Test
    public void shouldGenerateSha256PasswordHash() {
        MockitoAnnotations.initMocks(this);
        when(config.getSaltPhrase()).thenReturn("SecretSalt");
        
        String mail = "foo@bar.com";
        String password = "VerySecretPassword";
        
        String expectedHash = "d49f08dd079a9a041bb27af99d63545f2399e8668560d38b3b0e71a2d9a8c56e";
        String generatedHash = hashGenerator.generateHashedPassword(mail, password);
        
        Assert.assertThat(generatedHash, Is.is(IsEqual.equalTo(expectedHash)));
    }
}
