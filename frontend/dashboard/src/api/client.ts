export const API_BASE = process.env.REACT_APP_API_BASE ?? "";

export async function http<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const controller = new AbortController();
  const id = setTimeout(() => controller.abort(), 15000); // 15s timeout

  try {
    const res = await fetch(`${API_BASE}${path}`, {
      headers: { "Accept": "application/json", ...(options.headers ?? {}) },
      signal: controller.signal,
      ...options,
    });

    if (!res.ok) {
      const text = await res.text().catch(() => res.statusText);
      throw new Error(`HTTP ${res.status}: ${text}`);
    }
    return (await res.json()) as T;
  } finally {
    clearTimeout(id);
  }
}