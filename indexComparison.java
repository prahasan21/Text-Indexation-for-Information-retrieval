

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;



  
  
 public class indexComparison { 
	 
	 
	 
	 private static void Lucene_Indexer(Analyzer analyzer,String analyzer_type) throws IOException {
		 
		   	String corpusFile = "D:\\Search_JAVA\\corpus\\";
		 	String indexFile = "D:\\Search_JAVA\\index_file\\";
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			Directory dir = FSDirectory.open(Paths.get(indexFile));
			IndexWriter writer = new IndexWriter(dir, iwc);
			File[] all_files = new File(corpusFile).listFiles();
		 for(File file : all_files)
		 {
			 if (file.isFile())
			 {
			 
		 File f = new File(corpusFile+file.getName()+"");
		 //System.out.println("Converting File"+file.getName()+" to string....");
		 String each_file = FileUtils.readFileToString(f);
		 
			 


		  
		  String[] docs = StringUtils.substringsBetween(each_file, "<DOC>", "</DOC>");
		  
				for(int i=0;i<docs.length;i++)
			{
				
					Document luceneDoc = new Document();
					if(StringUtils.substringsBetween(docs[i], "<TEXT>", "</TEXT>")!=null)
					{
						String text = "";
						String TEXT[] = StringUtils.substringsBetween(docs[i], "<TEXT>", "</TEXT>");
						for(int t=0;t<TEXT.length;t++) 
						{
							text += " "+TEXT[t];
						}
						luceneDoc.add(new TextField("TEXT", text, Field.Store.YES));
					}
					
					
					writer.addDocument(luceneDoc);
					
				
				
				
			}
			
			


	 }
		 
		 		 
		 	 

}
	 	 

			writer.close();

			 IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
			 System.out.println(analyzer_type);
			 System.out.println("--------------------------------------------------------------------------------------------------------------");
			 Terms vocabulary = MultiFields.getTerms(reader, "TEXT");
			 TermsEnum iterator = vocabulary.iterator();
			 BytesRef byteRef = null;
			 int c = 0;
			//Vocabulary printing is commented as it is exceeding the Console window.
			//Please uncomment it if output needs to be checked.
			 //System.out.println("\n*******Vocabulary-Start**********");
			 while ((byteRef = iterator.next()) != null) {
				 String term = byteRef.utf8ToString();
//				 System.out.println(term);
				 c+=1;
			 }
		//System.out.println("\n*******Vocabulary-End**********");
				
				System.out.println("Total number of documents in the corpus:"+ reader.maxDoc());
				 System.out
					.println("Number of documents containing the term \"new\" for field \"TEXT\": "
							+ reader.docFreq(new Term("TEXT", "new")));
					System.out
					.println("Number of occurrences of \"new\" in the field\"TEXT\": "
							+ reader.totalTermFreq(new Term("TEXT", "new")));
					
							
					System.out.println("Size of the vocabulary for this field: "
							+ c);

					System.out
							.println("Number of documents that have at least one term for this field: "
									+ vocabulary.getDocCount());

					System.out.println("Number of tokens for this field: "
							+ vocabulary.getSumTotalTermFreq());

					System.out.println("Number of postings for this field: "
							+ vocabulary.getSumDocFreq());
					reader.close();
			
		}
	 
	 
	 
	 private static void analyse() throws IOException {
		    
		 Analyzer analyzer = null;         
	        //Keyword Analyzer
	        analyzer= new KeywordAnalyzer();        
	        Lucene_Indexer(analyzer, "Keyword Analyzer");
	        
	        //Simple Analyzer
	        analyzer= new SimpleAnalyzer();        
	        Lucene_Indexer(analyzer, "\nSimple Analyzer");
	        
	        //Simple Analyzer
	        analyzer= new StopAnalyzer();        
	        Lucene_Indexer(analyzer, "\nStop Analyzer");
	        
	        //Stop Analyzer
	        analyzer= new StandardAnalyzer();        
	        Lucene_Indexer(analyzer,  "\nStandard Analyzer");
	        
		  }
	 

	 public static void main(String[] args) throws IOException { 
		 
		 analyse();
		 	
 }
	 }
 
 
 
 
 