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

import java.util.Map;

/**
 *
 * IFileIndexerFactory manages multiple {@link IFileIndexer}.
 * 
 * @see {@link IFileIndexerFactory#getIndexer(String)}
 * @deprecated this interface is moved into the plugin-parser with another name {@code #fr.paris.lutece.plugins.parser.service.IParserFactory}
 * if you want use the new Interface, add the plugin-parser in the pom of your project
 */
@Deprecated
public interface IFileIndexerFactory
{
    /**
     * Bean name, to retrieve the factory from the Spring context.
     */
    String BEAN_FILE_INDEXER_FACTORY = "parser.parserFactory";

    /**
     * Sets the indexers map
     * 
     * @param mapIndexers
     *            the indexers to manage
     * @deprecated Use {@code #fr.paris.lutece.plugins.parser.service.IParserFactory#setParsersMap(Map<String, IStreamParser>)}
     */
    @Deprecated
    void setIndexersMap( Map<String, IFileIndexer> mapIndexers );

    /**
     * Returns the indexer matching the given MIMETYPE
     * 
     * @param strMimeType
     *            the mime type
     * @return the index found, <code>null</code> otherwise.
     * @deprecated Use {@code #fr.paris.lutece.plugins.parser.service.IParserFactory#getParser(String)}
     */
    @Deprecated
    IFileIndexer getIndexer( String strMimeType );
}
