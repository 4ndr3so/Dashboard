export default function ErrorBox({ message }: { message: string }) {
  return (
    <div style={{ padding: 12, color: "#b00020", border: "1px solid #f2c", borderRadius: 8 }}>
      {message}
    </div>
  );
}
