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
package fr.paris.lutece.plugins.lucene.service.indexer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * 
 * Wraps a tika indexer, such as {@link OfficeParser}
 */
public class TikaIndexer implements IFileIndexer
{
    private static final Logger _log = Logger.getLogger( "lutece.indexer" );
    private Parser _parser;

    /**
     * Sets the parse
     * 
     * @param parser
     *            the parser
     */
    public void setParser( Parser parser )
    {
        _parser = parser;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public String getContentToIndex( InputStream is )
    {
        String result = null;

        try
        {
            // limit set to -1 means no limit
            // see https://issues.apache.org/jira/browse/TIKA-557
            ContentHandler handler = new BodyContentHandler( -1 );
            Metadata metadata = new Metadata( );

            // context : can add more infos (like Local, for Excel datasheets)
            ParseContext parseContext = new ParseContext( );
            _parser.parse( is, handler, metadata, parseContext );

            result = handler.toString( );
        }
        catch( IOException ex )
        {
            _log.error( ex.getMessage( ), ex );
        }
        catch( SAXException ex )
        {
            _log.error( ex.getMessage( ), ex );
        }
        catch( TikaException ex )
        {
            _log.error( ex.getMessage( ), ex );
        }

        return result;
    }
}
