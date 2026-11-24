/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.Test;

/**
 * French elision of the analyzer: the elided articles are removed from the indexed and searched words
 */
public class LuteceFrenchAnalyzerTest
{
    private static List<String> tokens( Analyzer analyzer, String strText ) throws IOException
    {
        List<String> listTokens = new ArrayList<>( );
        try ( TokenStream stream = analyzer.tokenStream( "contents", strText ) )
        {
            CharTermAttribute term = stream.addAttribute( CharTermAttribute.class );
            stream.reset( );
            while ( stream.incrementToken( ) )
            {
                listTokens.add( term.toString( ) );
            }
            stream.end( );
        }
        return listTokens;
    }

    /**
     * Every French elided article gives the same token as the word alone
     */
    @Test
    public void testElidedArticles( ) throws IOException
    {
        try ( Analyzer analyzer = new LuteceFrenchAnalyzer( ) )
        {
            assertEquals( tokens( analyzer, "abonne" ), tokens( analyzer, "m'abonne" ) );
            assertEquals( tokens( analyzer, "abonne" ), tokens( analyzer, "t'abonne" ) );
            assertEquals( tokens( analyzer, "aucun" ), tokens( analyzer, "puisqu'aucun" ) );
            assertEquals( tokens( analyzer, "arrive" ), tokens( analyzer, "lorsqu'arrive" ) );
            assertEquals( tokens( analyzer, "inscription" ), tokens( analyzer, "l'inscription" ) );
        }
    }
}
