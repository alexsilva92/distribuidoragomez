/*
 * Fichero: PasswordHasher.java
 * Autor: Jose Manuel Cejudo Gausi (jmc@jmcejudo.com)
 * 
 * Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los
 * términos de la Licencia EUPL European Public License publicada por el organismo IDABC de la
 * Comisión Europea, en su versión 1.0. o posteriores.
 * 
 * Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas
 * garantías implícitas de USABILIDAD o ADECUACIÓN A PROPÓSITO CONCRETO. Para mas información
 * consulte la Licencia EUPL European Public License.
 * 
 * Usted recibe una copia de la Licencia EUPL European Public License junto con este programa,
 * si por algún motivo no le es posible visualizarla, puede consultarla en la siguiente
 * URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099
 * 
 * You should have received a copy of the EUPL European Public License along with this program.
 * If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096
 * 
 * Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme.
 * Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205
 * 
 * Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm.
 * Wenn nicht, finden Sie da http://ec.europa.eu/idabc/servlets/Doc?id=29919
 * 
 */

package com.utilidades.crypto;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



/**
 * Implementación simple de métodos de utilidad para generar códigos <code>hash</code> con
 * <code>salt</code>.
 * <p>
 * Esta clase utiliza los siguientes parámetros por defecto:
 * <ul>
 * <li>Algoritmo de hash: HmacSHA512</li>
 * <li>Longitud del <code>salt</code>: 64 bytes</li>
 * <li>Iteraciones: 1000</li>
 * <li>Conjunto de caracteres para codificación de cadenas: UTF-8</li>
 * </ul>
 * <p>
 * Ejemplo:
 * 
 * <pre>
 * // Crea el hash de &lt;code&gt;valor&lt;/code&gt;
 * PasswordHash ph = PasswordHasher.hash(valor);
 * // Comprueba si un &lt;code&gt;valor&lt;/code&gt; concuerda con un &lt;code&gt;hash&lt;/code&gt;
 * boolean valido = PasswordHasher.isValid(valor, ph.getHash(), ph.getSalt());
 * </pre>
 * <p>
 * NOTA: esta clase es un ejemplo de implementación, aunque puede usarse en un entorno de
 * producción. Para simplificar su uso todas las excepciones chequeadas se capturan y se relanzan
 * como {@link RuntimeException}. En una implementación más completa podría ser conveniente definir
 * excepciones específicas.
 * 
 * @author Jose Manuel Cejudo Gausi
 */
public final class PasswordHasher {

    /**
     * Nombre del algoritmo de hash.
     */
    public static final String ALGORITHM = "HmacSHA512";

    /**
     * Longitud del <code>salt</code>. Igual al tamaño del resultado del algoritmo.
     */
    public static final int SALT_LENGTH = 64;

    /**
     * Número de iteraciones a realizar.
     */
    public static final int ITERATIONS = 1000;

    /**
     * Nombre del conjunto de caracteres para codificar cadenas de texto.
     */
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * Representa el resultado de una operación de <code>hash</code> con <code>salt</code>.
     * 
     * @author Jose Manuel Cejudo Gausi
     */
    public static class PasswordHash {

        /**
         * Contiene el valor de <code>hash</code>.
         */
        private final byte[] hash;

        /**
         * Contiene el valor de <code>salt</code>.
         */
        private final byte[] salt;

        /**
         * Construye una nueva instancia con los valores indicados.
         * 
         * @param hash
         *            el valor de <code>hash</code>.
         * @param salt
         *            el valor de <code>salt</code>.
         */
        public PasswordHash(final byte[] hash, final byte[] salt) {

            this.hash = hash;
            this.salt = salt;

        }

        /**
         * Devuelve el valor de <code>hash</code>.
         * 
         * @return el valor.
         */
        public byte[] getHash() {

            return hash;

        }

        /**
         * Devuelve el valor de <code>salt</code>.
         * 
         * @return el valor.
         */
        public byte[] getSalt() {

            return salt;

        }

        /**
         * Devuelve el valor de <code>hash</code>.
         * 
         * @return el valor.
         */
        public String getHashString() {

            //return Base64.encode(hash);
            return DatatypeConverter.printBase64Binary(hash);

        }

        /**
         * Devuelve el valor de <code>salt</code>.
         * 
         * @return el valor.
         */
        public String getSaltString() {

            //return Base64.encode(salt);
            return DatatypeConverter.printBase64Binary(salt);

        }

    }

    /**
     * Constructor privado para impedir la instanciación de esta clase de utilidad.
     */
    private PasswordHasher() {

        super();

    }

    /**
     * Calcula el <code>hash</code> del valor indicado.
     * <p>
     * Este método genera un valor de <code>salt</code> aleatorio y lo utiliza para calcular
     * repetitivamente tantas operaciones de <code>hash</code> como indique {@link #ITERATIONS}.
     * <p>
     * El objeto {@link PasswordHash} devuelto contiene el <code>hash</code> calculado y el
     * <code>salt</code> generado.
     * 
     * @param value
     *            el valor para el que calcular el <code>hash</code>.
     * @return el resultado del cálculo <code>hash</code>.
     */
    public static PasswordHash hash(final String value) {

        final byte[] salt = new byte[SALT_LENGTH];
        final SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(salt);

        return hash(value, salt);

    }

    /**
     * Calcula el <code>hash</code> del valor indicado usando el <code>salt</code> indicado.
     * <p>
     * El objeto {@link PasswordHash} devuelto contiene el <code>hash</code> calculado y el
     * <code>salt</code> generado.
     * 
     * @param value
     *            el valor para el que calcular el <code>hash</code>.
     * @param salt
     *            el valor de <code>salt</code> a utilizar.
     * @return el resultado del cálculo <code>hash</code>.
     */
    public static PasswordHash hash(final String value, final byte[] salt) {

        try {
            byte[] retVal;
            final byte[] valueBytes = value.getBytes(CHARSET_NAME);
            final Mac mac = Mac.getInstance(ALGORITHM);
            final Key key = new SecretKeySpec(salt, ALGORITHM);
            mac.init(key);
            retVal = mac.doFinal(valueBytes);
            for (int i = 1; i < ITERATIONS; i++) {
                retVal = mac.doFinal(retVal);
            }
            return new PasswordHash(retVal, salt);

        } catch (final NoSuchAlgorithmException cause) {
            throw new RuntimeException(cause);
        } catch (final InvalidKeyException cause) {
            throw new RuntimeException(cause);
        } catch (final UnsupportedEncodingException cause) {
            throw new RuntimeException(cause);
        }

    }

    /**
     * Comprueba si el <code>hash</code> calculado para el valor y el <code>salt</code> indicados es
     * igual al <code>hash</code> correcto indicado.
     * <p>
     * Este método permite comprobar si un valor es integro con respecto al <code>hash</code>.
     * 
     * @param value
     *            el valor a comprobar.
     * @param correctHash
     *            el <code>hash</code> correcto.
     * @param salt
     *            el <code>salt</code> a utilizar para la generación del <code>hash</code> de
     *            <code>value</code>. Debe ser el mismo que el utilizado para calcular
     *            <code>hash</code>.
     * @return <code>true</code> si el <code>hash</code> de <code>value</code> usando
     *         <code>salt</code> es igual a <code>correctHash</code>.
     */
    public static boolean isValid(final String value, final byte[] correctHash, final byte[] salt) {

        final PasswordHash ph = hash(value, salt);
        return Arrays.equals(ph.getHash(), correctHash);

    }
    
    public static boolean isValid(final String value, String correctHash, String salt) {
        final PasswordHash ph = hash(value, DatatypeConverter.parseBase64Binary(salt));
        return Arrays.equals(ph.getHash(), DatatypeConverter.parseBase64Binary(correctHash));
    }
}
