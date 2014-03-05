/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.lucene.service.analyzer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.ElisionFilter;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.FrenchStemmer;


/**
 * 
 * {@link FrenchAnalyzer}, adding {@link ElisionFilter} and
 * {@link ASCIIFoldingFilter}. <br>
 * <ol>
 * <li>{@link StandardTokenizer}</li>
 * <li>{@link ElisionFilter}</li>
 * <li>{@link StandardFilter}</li>
 * <li>{@link StopFilter}</li>
 * <li>{@link ASCIIFoldingFilter}</li>
 * <li>{@link LowerCaseFilter}</li>
 * </ol>
 * @see FrenchAnalyzer
 * @see ElisionFilter
 * @see ASCIIFoldingFilter
 * @see Analyzer
 * 
 */
public class LuteceFrenchAnalyzer extends Analyzer
{
    /**
     * Extended list of typical French stopwords.
     */
    public static final String[] FRENCH_STOP_WORDS = { "a", "afin", "ai", "ainsi", "après", "attendu", "au", "aujourd",
            "auquel", "aussi", "autre", "autres", "aux", "auxquelles", "auxquels", "avait", "avant", "avec", "avoir",
            "c", "car", "ce", "ceci", "cela", "celle", "celles", "celui", "cependant", "certain", "certaine",
            "certaines", "certains", "ces", "cet", "cette", "ceux", "chez", "ci", "combien", "comme", "comment",
            "concernant", "contre", "d", "dans", "de", "debout", "dedans", "dehors", "delà", "depuis", "derrière",
            "des", "désormais", "desquelles", "desquels", "dessous", "dessus", "devant", "devers", "devra", "divers",
            "diverse", "diverses", "doit", "donc", "dont", "du", "duquel", "durant", "dès", "elle", "elles", "en",
            "entre", "environ", "est", "et", "etc", "etre", "eu", "eux", "excepté", "hormis", "hors", "hélas", "hui",
            "il", "ils", "j", "je", "jusqu", "jusque", "l", "la", "laquelle", "le", "lequel", "les", "lesquelles",
            "lesquels", "leur", "leurs", "lorsque", "lui", "là", "ma", "mais", "malgré", "me", "merci", "mes", "mien",
            "mienne", "miennes", "miens", "moi", "moins", "mon", "moyennant", "même", "mêmes", "n", "ne", "ni", "non",
            "nos", "notre", "nous", "néanmoins", "nôtre", "nôtres", "on", "ont", "ou", "outre", "où", "par", "parmi",
            "partant", "pas", "passé", "pendant", "plein", "plus", "plusieurs", "pour", "pourquoi", "proche", "près",
            "puisque", "qu", "quand", "que", "quel", "quelle", "quelles", "quels", "qui", "quoi", "quoique", "revoici",
            "revoilà", "s", "sa", "sans", "sauf", "se", "selon", "seront", "ses", "si", "sien", "sienne", "siennes",
            "siens", "sinon", "soi", "soit", "son", "sont", "sous", "suivant", "sur", "ta", "te", "tes", "tien",
            "tienne", "tiennes", "tiens", "toi", "ton", "tous", "tout", "toute", "toutes", "tu", "un", "une", "va",
            "vers", "voici", "voilà", "vos", "votre", "vous", "vu", "vôtre", "vôtres", "y", "à", "ça", "ès", "été",
            "être", "ô", };

    /**
     * Contains the stopwords used with the {@link StopFilter}.
     */
    private CharArraySet _stoptable;

    private final Version _matchVersion;

    /**
     * Builds an analyzer with the default stop words (
     * {@link #FRENCH_STOP_WORDS}).
     */
    public LuteceFrenchAnalyzer( )
    {
        this( Version.LUCENE_46 );
    }

    /**
     * Builds an analyzer with the default stop words (
     * {@link #FRENCH_STOP_WORDS}).
     * @param matchVersion the version
     */
    public LuteceFrenchAnalyzer( Version matchVersion )
    {
        _stoptable = StopFilter.makeStopSet( matchVersion, FRENCH_STOP_WORDS, false );
        this._matchVersion = matchVersion;
    }

    /**
     * Builds an analyzer with the given stop words.
     * @param matchVersion the version
     * @param stopwords the stop words
     */
    public LuteceFrenchAnalyzer( Version matchVersion, String[] stopwords )
    {
        _stoptable = StopFilter.makeStopSet( matchVersion, stopwords );
        this._matchVersion = matchVersion;
    }

    /**
     * Builds an analyzer with the given stop words.
     * @param matchVersion the version
     * @param stopwords the stop words
     * @throws IOException io exception
     */
    public LuteceFrenchAnalyzer( Version matchVersion, File stopwords ) throws IOException
    {
        _stoptable = WordlistLoader.getWordSet( new FileReader( stopwords ), matchVersion );
        this._matchVersion = matchVersion;
    }

    @Override
    protected TokenStreamComponents createComponents( String fieldName, Reader reader )
    {
        if ( fieldName == null )
        {
            throw new IllegalArgumentException( "fieldName must not be null" );
        }

        if ( reader == null )
        {
            throw new IllegalArgumentException( "reader must not be null" );
        }

        Tokenizer source = new StandardTokenizer( _matchVersion, reader );
        TokenStream filter = new StandardFilter( _matchVersion, source );
        filter = new ElisionFilter( filter, _stoptable );
        filter = new StopFilter( _matchVersion, filter, _stoptable );
        filter = new ASCIIFoldingFilter( filter );
        filter = new SnowballFilter( filter, new FrenchStemmer( ) );
        // Convert to lowercase after stemming!
        filter = new LowerCaseFilter( _matchVersion, filter );

        return new TokenStreamComponents( source, filter )
        {
            @Override
            protected void setReader( final Reader reader ) throws IOException
            {
                super.setReader( reader );
            }
        };

    }
}
