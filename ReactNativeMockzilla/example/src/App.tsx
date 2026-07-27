import React, { useEffect, useState } from 'react';
import { Button, StyleSheet, Text, View } from 'react-native';
import { HttpMethod, Mockzilla } from 'react-native-mockzilla';
import type { MockzillaRuntimeParams } from 'react-native-mockzilla';

export default function App() {
  const [params, setParams] = useState<MockzillaRuntimeParams | null>(null);
  const [response, setResponse] = useState<string | null>(null);

  useEffect(() => {
    Mockzilla.startMockzilla({
      endpoints: [
        {
          key: 'getUser',
          endpointMatcher: (req) =>
            req.uri.endsWith('/user') && req.method === HttpMethod.GET,
          defaultHandler: (_req) => ({
            statusCode: 200,
            body: JSON.stringify({ id: 1, name: 'Test User' }),
          }),
          errorHandler: (_req) => ({
            statusCode: 500,
            body: JSON.stringify({ error: 'Server Error' }),
          }),
          dashboardOptionsConfig: {
            presets: [
              {
                name: 'Other person',
                response: {
                  statusCode: 200,
                  body: JSON.stringify({ id: 2, name: 'Other person' }),
                },
              },
              {
                name: 'Not Found',
                response: {
                  statusCode: 404,
                  body: JSON.stringify({ error: 'Not Found' }),
                },
              },
              {
                name: 'Unauthorized',
                description: 'Simulates an expired/invalid auth token',
                response: {
                  statusCode: 401,
                  body: JSON.stringify({ error: 'Unauthorized' }),
                },
              },
              {
                name: 'Rate Limited',
                description: 'Too many requests, with a Retry-After header',
                response: {
                  statusCode: 429,
                  headers: { 'Retry-After': '30' },
                  body: JSON.stringify({ error: 'Too Many Requests' }),
                },
              },
              {
                name: 'Malformed JSON',
                description: 'Success status but an invalid JSON body',
                response: {
                  statusCode: 200,
                  body: '{ id: 1, name: "Test User"',
                },
              },
              {
                name: 'Empty Response',
                description: '204 with no body',
                response: {
                  statusCode: 204,
                  body: '',
                },
              },
            ],
          },
        },
      ],
    }).then(setParams);

    return () => {
      Mockzilla.stopMockzilla();
    };
  }, []);

  const fetchUser = async () => {
    if (!params) return;
    try {
      const res = await fetch(`${params.mockBaseUrl}/user`);
      const text = await res.text();
      setResponse(`${res.status}: ${text}`);
    } catch (e) {
      setResponse(`Error: ${e}`);
    }
  };

  return (
    <View style={styles.container}>
      <Text>Status: {params ? 'Running' : 'Starting...'}</Text>
      {params && <Text>Mock URL: {params.mockBaseUrl}</Text>}
      <Button title="Fetch /user" onPress={fetchUser} disabled={!params} />
      {response && <Text style={styles.response}>{response}</Text>}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 12 },
  response: { fontFamily: 'monospace', textAlign: 'center', paddingHorizontal: 16 },
});
