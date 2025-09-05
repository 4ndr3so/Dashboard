export default function Loading({ text = "Loading..." }: { text?: string }) {
  return <div style={{ padding: 12, opacity: 0.8 }}>{text}</div>;
}