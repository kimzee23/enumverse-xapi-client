package org.enums.query;

import org.enums.client.XapiClientConfig;
import org.enums.client.XapiHttpClient;

import static org.junit.jupiter.api.Assertions.*;

class CursorIteratorTest {
        public static void main(String[] args) throws Exception {

            XapiClientConfig config = new XapiClientConfig(
                    "https://lrs.example.com/xapi",
                    "username",
                    "password",
                    5
            );

            XapiHttpClient http = new XapiHttpClient(config);

            XapiQueryClient queryClient = new XapiQueryClient(config, http);

            QueryParams params =
                    TypedQuery.create()
                            .verb("https://adlnet.gov/expapi/verbs/completed")
                            .limit(50)
                            .toParams();

            QueryResult result = queryClient.queryStatements(params);

            result.getStatements().forEach(System.out::println);

            CursorIterator it = new CursorIterator(queryClient, params);
            while (it.hasNext()) {
                it.next().forEach(System.out::println);
            }
        }
    }